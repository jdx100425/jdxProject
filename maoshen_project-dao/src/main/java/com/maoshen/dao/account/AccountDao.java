package com.maoshen.dao.account;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.maoshen.dao.account.entity.Account;

@Repository
public interface AccountDao {
	public Account selectByUserNameAndPwd(@Param("userName") String userName, @Param("password") String password);
}
