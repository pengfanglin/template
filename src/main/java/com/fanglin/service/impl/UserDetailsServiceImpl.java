package com.fanglin.service.impl;

import com.fanglin.entity.system.AccountEntity;
import com.fanglin.mapper.MapperFactory;
import com.fanglin.security.Token;
import com.fanglin.utils.OthersUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * security获取用户权限信息
 * @author 彭方林
 * @date 2019/4/3 19:05
 * @version 1.0
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private MapperFactory mapperFactory;

    @Override
    public UserDetails loadUserByUsername(String username) {
        AccountEntity systemAccount = mapperFactory.systemAccountMapper.selectOne(new AccountEntity().setUsername(username));
        if (systemAccount == null) {
            throw new UsernameNotFoundException("账号不存在");
        }
        //比如用户账号已停用判断。
        if ("1".equals(systemAccount.getIsDisable())) {
            throw new DisabledException("账号已禁用");
        }
        Token token = new Token()
            .setUsername(systemAccount.getUsername())
            .setPassword(systemAccount.getPassword())
            .setId(systemAccount.getAccountId())
            .setDisable("1".equals(systemAccount.getIsDisable()))
            .setTokenTime(new Date());
        //查询该用户所拥有的角色+权限
        String authInfo= mapperFactory.othersMapper.getAuthInfo(systemAccount.getRoleIds());
        if(!OthersUtils.isEmpty(authInfo)){
            List<GrantedAuthority> grantedAuthorities=new LinkedList<>();
            for(String role:authInfo.split(",")){
                grantedAuthorities.add(new SimpleGrantedAuthority(role));
            }
            token.setAuthorities(grantedAuthorities);
        }
        return token;
    }
}
