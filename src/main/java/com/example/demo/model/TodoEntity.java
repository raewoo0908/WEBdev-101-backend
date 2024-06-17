package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity //테이블과 링크될 클래스임을 명시한다.
@Table(name = "Todo") //테이블 이름을 지정한다. 이 엔티티가 데이터베이스의 Todo 테이블에 매핑된다는 뜻이다.
public class TodoEntity {
    @Id //기본 키가 될 필드 위에 지정.
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id; //이 오브젝트의 아이디
    private String userId; //이 오브젝트를 생성한 유저의 아이디
    private String title; //Todo 타이틀 예) 운동하기
    private boolean done; //true-todo를 완료한 경우(checked)
}
