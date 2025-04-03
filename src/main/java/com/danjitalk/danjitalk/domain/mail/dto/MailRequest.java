package com.danjitalk.danjitalk.domain.mail.dto;

import com.danjitalk.danjitalk.domain.mail.enums.MailType;

public record MailRequest(
    String mail,
    MailType type
) {

}
