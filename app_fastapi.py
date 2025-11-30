# app_fastapi.py
from fastapi import FastAPI, HTTPException, Request, Response, status
from fastapi.responses import JSONResponse
import time
from typing import List, Dict, Any

import schemas

# ---------- FastAPI 앱 ----------
app = FastAPI(title="HTTP Methods Assignment API (In-Memory)")

# ---------- In-Memory DB (메모리 내 데이터베이스) ----------
# 서버가 실행되는 동안에만 데이터를 저장하는 '가짜' 데이터베이스
fake_users_db: List[Dict[str, Any]] = []
user_id_counter = 0

# ---------- 공통 응답 포맷 유틸 ----------
def success_response(data=None, message: str = "OK"):
    if data is None:
        data = {}
    return {"status": "success", "data": data, "message": message}

def error_response(message: str, data=None):
    if data is None:
        data = {}
    return {"status": "error", "data": data, "message": message}

# ---------- 전역 예외 핸들러 ----------
@app.exception_handler(HTTPException)
async def http_exception_handler(request: Request, exc: HTTPException):
    return JSONResponse(
        status_code=exc.status_code,
        content=error_response(message=exc.detail)
    )

# ---------- 미들웨어 (요청 로그 + 처리 시간 측정) ----------
@app.middleware("http")
async def log_request_middleware(request: Request, call_next):
    start = time.time()
    method = request.method
    path = request.url.path
    print(f"[REQUEST] {method} {path}")
    try:
        response = await call_next(request)
    except Exception as e:
        print(f"[ERROR] {method} {path} -> {e}")
        raise
    process_time = (time.time() - start) * 1000
    response.headers["X-Process-Time-ms"] = f"{process_time:.2f}"
    print(f"[RESPONSE] {method} {path} -> {response.status_code} ({process_time:.2f} ms)")
    return response

# ---------- Helper 함수 ----------
def find_user_by_id(user_id: int):
    """메모리 DB에서 ID로 유저 찾기"""
    for user in fake_users_db:
        if user["id"] == user_id:
            return user
    return None

# =============== POST 메소드 2개 ===============

# 1) POST /users : 유저 생성 (201 or 400)
@app.post("/users", status_code=status.HTTP_201_CREATED)
def create_user(user: schemas.UserCreate):
    global user_id_counter
    # 같은 email/username이 있는지 확인
    for existing_user in fake_users_db:
        if existing_user["email"] == user.email or existing_user["username"] == user.username:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="이 email 또는 username은 이미 존재합니다.",
            )
    
    user_id_counter += 1
    new_user = {
        "id": user_id_counter,
        "email": user.email,
        "username": user.username,
        "is_active": True
    }
    fake_users_db.append(new_user)
    
    return success_response(data=schemas.UserRead(**new_user), message="유저 생성 완료")

# 2) POST /users/{user_id}/deactivate : 비활성화 (200 or 404)
@app.post("/users/{user_id}/deactivate")
def deactivate_user(user_id: int):
    user = find_user_by_id(user_id)
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="해당 ID의 유저를 찾을 수 없습니다.")
    
    user["is_active"] = False
    return success_response(data=schemas.UserRead(**user), message="유저 비활성화 완료")

# =============== GET 메소드 2개 ===============

# 3) GET /users : 전체 유저 목록 조회 (200)
@app.get("/users")
def list_users():
    return success_response(data=[schemas.UserRead(**u) for u in fake_users_db], message="유저 목록 조회 완료")

# 4) GET /debug/error : 500 / 503 같은 5xx 테스트용
@app.get("/debug/error")
def debug_error(error_type: int = 500):
    if error_type == 500:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail="의도적으로 발생시킨 500 서버 에러입니다.")
    elif error_type == 503:
        raise HTTPException(status_code=status.HTTP_503_SERVICE_UNAVAILABLE, detail="서비스 점검 중입니다. (의도된 503 에러)")
    else:
        return success_response(message="error_type은 500 또는 503만 지원합니다.")

# =============== PUT 메소드 2개 ===============

# 5) PUT /users/{user_id} : 유저 정보 수정 (200 or 404)
@app.put("/users/{user_id}")
def update_user(user_id: int, update: schemas.UserUpdate):
    user = find_user_by_id(user_id)
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="수정하려는 유저를 찾을 수 없습니다.")

    update_data = update.dict(exclude_unset=True)
    for key, value in update_data.items():
        user[key] = value
        
    return success_response(data=schemas.UserRead(**user), message="유저 정보 수정 완료")

# 6) PUT /users/{user_id}/activate : 다시 활성화 (200 or 404)
@app.put("/users/{user_id}/activate")
def activate_user(user_id: int):
    user = find_user_by_id(user_id)
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="활성화하려는 유저를 찾을 수 없습니다.")
    
    user["is_active"] = True
    return success_response(data=schemas.UserRead(**user), message="유저 활성화 완료")

# =============== DELETE 메소드 2개 ===============

# 7) DELETE /users/{user_id} : 소프트 삭제 (is_active=False) (200 or 404)
@app.delete("/users/{user_id}")
def delete_user_soft(user_id: int):
    user = find_user_by_id(user_id)
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="삭제하려는 유저를 찾을 수 없습니다.")
    
    user["is_active"] = False
    return success_response(data=schemas.UserRead(**user), message="유저 soft delete (비활성화) 완료")

# 8) DELETE /users/{user_id}/hard : 실제 메모리에서 삭제 (200 or 404)
@app.delete("/users/{user_id}/hard")
def delete_user_hard(user_id: int):
    user = find_user_by_id(user_id)
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="hard delete 하려는 유저를 찾을 수 없습니다.")
    
    fake_users_db.remove(user)
    return success_response(message="유저를 완전히 삭제했습니다.")
