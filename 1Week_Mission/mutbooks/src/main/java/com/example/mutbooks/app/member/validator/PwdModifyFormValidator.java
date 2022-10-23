package com.example.mutbooks.app.member.validator;

import com.example.mutbooks.app.member.form.PwdModifyForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PwdModifyFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PwdModifyForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PwdModifyForm pwdModifyForm = (PwdModifyForm) target;
        if(!pwdModifyForm.getNewPassword().equals(pwdModifyForm.getNewPasswordConfirm())) {
            errors.rejectValue("newPasswordConfirm", "notMatchNewPasswordAndNewPasswordConfirm", "입력한 새 비밀번호가 일치하지 않습니다.");
        }

        if(pwdModifyForm.getNewPassword().equals(pwdModifyForm.getPassword())) {
            errors.rejectValue("newPassword", "samePassword", "기존 비밀번호와 일치합니다.");
        }
    }
}
