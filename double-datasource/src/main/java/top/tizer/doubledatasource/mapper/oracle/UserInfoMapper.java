package top.tizer.doubledatasource.mapper.oracle;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.tizer.doubledatasource.entity.UserInfo;
import top.tizer.doubledatasource.entity.UserInfoExample;

@Mapper
@Repository
public interface UserInfoMapper {
    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    List<UserInfo> selectByExample(UserInfoExample example);

    List<UserInfo> selectAll();
}