package com.praver.springboot.mapper;

import com.praver.springboot.controller.request.BaseRequest;
import com.praver.springboot.entity.Borrow;
import com.praver.springboot.entity.Retur;
import com.praver.springboot.mapper.op.BorrowReturCountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BorrowMapper {
    List<Borrow> list();

    List<Borrow> listByCondition(BaseRequest baseRequest);

    List<Retur> listReturByCondition(BaseRequest baseRequest);

    void save(Borrow obj);

    void saveRetur(Retur obj);

    Borrow getById(Integer id);

    void updateById(Borrow user);

    void deleteById(Integer id);

    void deleteReturById(Integer id);

    void updateStatus(String status, Integer id);

    List<Borrow> getByBookNo(String bookNo);

    List<BorrowReturCountPO> getCountByTimeRange(@Param("timeRange") String timeRange, @Param("type") int type);
}
