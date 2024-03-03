package com.example.demo.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {

    private List<UserResponse> users;

    private int currentPage;

    private long totalItems;

    private int totalPages;
}
