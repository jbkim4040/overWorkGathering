package com.overWorkGathering.main.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Constant {

    @Getter
    @AllArgsConstructor
    public enum Auth{
        M("M", "관리자"),
        U("U", "일반사용자"),
        L("L", "파트리더"),
        E("E", "경영팀");

        public String code;
        public String desc;
    }
}
