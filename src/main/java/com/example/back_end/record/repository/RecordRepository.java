package com.example.back_end.record.repository;

import com.example.back_end.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByOrderByCreateAtDesc(); //최신순 정렬

}
