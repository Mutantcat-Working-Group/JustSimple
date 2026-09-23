package com.swagger.demo.controller.restapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mutantcat.justsimple.annotation.*;
import org.mutantcat.justsimple.core.handle.Result;
import org.mutantcat.justsimple.validation.annotation.Valid;
import org.mutantcat.justsimple.validation.annotation.Validated;

@Valid
@Controller
@Mapping("user")
@Tag(name = "用户接口")
public class UserController {

    @Post
    @Mapping
    @Operation(summary = "添加用户")
    public Result<Long> add(@Validated @Body UserAddDTO dto) {
        return Result.succeed(1L);
    }

    @Put
    @Mapping
    @Operation(summary = "编辑用户")
    public Result<Void> edit(@Validated @Body UserEditDTO dto) {
        return Result.succeed();
    }
}