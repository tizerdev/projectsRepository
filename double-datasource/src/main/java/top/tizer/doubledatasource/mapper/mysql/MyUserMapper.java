package top.tizer.doubledatasource.mapper.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.tizer.doubledatasource.entity.MyUser;
import top.tizer.doubledatasource.entity.MyUserExample;

@Mapper
@Repository
public interface MyUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MyUser record);

    int insertSelective(MyUser record);

    List<MyUser> selectByExample(MyUserExample example);

    MyUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MyUser record);

    int updateByPrimaryKey(MyUser record);
}