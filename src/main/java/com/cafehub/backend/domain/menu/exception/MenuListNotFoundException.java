package com.cafehub.backend.domain.menu.exception;

import com.cafehub.backend.common.exception.CafeHubException;
import com.cafehub.backend.domain.menu.exception.code.MenuExceptionCode;

public class MenuListNotFoundException extends CafeHubException {
    public MenuListNotFoundException() {
        super(MenuExceptionCode.MENU_LIST_NOT_FOUND);
    }
}
