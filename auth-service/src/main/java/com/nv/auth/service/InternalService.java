package com.nv.auth.service;

import com.nv.auth.dto.InternalUserResponse;

public interface InternalService {
	 InternalUserResponse getUserByEmail(String email);
}
