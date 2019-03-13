package com.sou1fy.test.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper {

    @Insert("insert into sa_dictionary(dicid,dictname,dictgroup)valuse(#{dictId},#{dictName},#{dictGroup})")
    int insertTestData(@Param("dictId") String dictId, @Param("dictName") String dictName, @Param("dictGroup") String dictGroup);

    @Delete("delete from sa_dictionary where dictid=#{dictId} and dictgroup=#{dictGroup}")
    int deleteTestData(@Param("dictId") String dictId, @Param("dictGroup") String dictGroup);

}
