package com.minsu.webservice.domain.user;

import com.minsu.webservice.domain.user.dto.AdminChangeRoleRequest;
import com.minsu.webservice.domain.user.dto.UserResponse;
import com.minsu.webservice.global.error.ApiResponse;
import com.minsu.webservice.global.dto.PageRequest;
import com.minsu.webservice.global.dto.SortCriteria;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<Page<UserResponse>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id,DESC") String sort
    ) {
        com.minsu.webservice.global.dto.PageRequest pageRequest = new com.minsu.webservice.global.dto.PageRequest(page, size);
        com.minsu.webservice.global.dto.SortCriteria sortCriteria = new com.minsu.webservice.global.dto.SortCriteria(sort);

        return ApiResponse.ok("ADMIN_USER_LIST", "User list", userService.adminList(keyword, role, pageRequest, sortCriteria));
    }

    @PatchMapping("/{id}/deactivate")
    public ApiResponse<Void> deactivate(@PathVariable Long id) {
        userService.adminDeactivate(id);
        return ApiResponse.ok("ADMIN_USER_DEACTIVATED", "User deactivated");
    }

    @PatchMapping("/{id}/role")
    public ApiResponse<Void> changeRole(@PathVariable Long id, @RequestBody @Valid AdminChangeRoleRequest req) {
        userService.adminChangeRole(id, req.role());
        return ApiResponse.ok("ADMIN_USER_ROLE_CHANGED", "User role changed", null);
    }
}
