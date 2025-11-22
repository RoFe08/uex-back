package org.uex_back.dto.contact;

import java.util.List;

public record ContactPageResponse(
        List<ContactResponse> content,
        long totalElements,
        int totalPages,
        int page,
        int size
) {}