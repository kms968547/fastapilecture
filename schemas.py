from pydantic import BaseModel, EmailStr
from typing import Optional


class UserBase(BaseModel):
    email: EmailStr
    username: str


class UserCreate(UserBase):
    """POST /users 에서 쓸 입력 스키마"""
    pass


class UserUpdate(BaseModel):
    """PUT /users/{id} 에서 쓸 입력 스키마"""
    email: Optional[EmailStr] = None
    username: Optional[str] = None
    is_active: Optional[bool] = None


class UserRead(UserBase):
    """응답용 스키마"""
    id: int
    is_active: bool

    class Config:
        pass
        # orm_mode = True # ORM 모드는 데이터베이스를 사용하지 않으므로 제거
