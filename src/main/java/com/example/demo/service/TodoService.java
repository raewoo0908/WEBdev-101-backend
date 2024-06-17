package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public String testService(){
        //TodoEntity 생성
        TodoEntity entity = TodoEntity.builder().title("My first todo item.").build();
        //TodoEntity 저장
        repository.save(entity);
        //TodoEntity 검색
        TodoEntity savedEntity = repository.findById(entity.getId()).get();
        return savedEntity.getTitle();
    }

    public void validate(final TodoEntity entity){
        if (entity == null){
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if (entity.getUserId() == null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

    public List<TodoEntity> create(final TodoEntity entity){
        //Validation: 넘어온 entity가 유효한 지 검사.
        validate(entity);

        //save: entity를 데이터베이스에 저장한다.
        repository.save(entity);
        log.info("Entity Id: {} is saved.", entity.getId());

        //저장된 entity를 포함하는 새로운 리스트를 return한다.
        return repository.findByUserId(entity.getUserId());
    }

    //repository에서 해당 userID에 해당하는 TodoEntity를 리스트로 리턴하는 메소드.
    public List<TodoEntity> retrieve(final String userId){
        return repository.findByUserId(userId);
    }

    //기존에 존재하는 TodoEntity를 새로운 entity로 업데이트하는 메소드.
    public List<TodoEntity> update(final TodoEntity entity){
        //(1) 저장할 엔티티가 유효한 지 확인한다.
        validate(entity);

        //(2) 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트 할 수 없기 때문이다.
        final Optional<TodoEntity> original = repository.findById(entity.getId());


        if (original.isPresent()){
            //(3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
            final TodoEntity todo = original.get();
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            //(4) 데이터베이스에 새 값을 저장한다.
            repository.save(todo);
        }

        //유저의 모든 Todo 리스트를 리턴한다.
        return retrieve(entity.getUserId());
    }

    //해당 entity를 삭제하고 새로운 list를 리턴한다.
    public List<TodoEntity> delete(final TodoEntity entity){
        //(1) 지우고 하는 entity가 유효한 지 확인한다.
        validate(entity);

        try{
            //(2) 엔티티를 삭제한다.
            repository.delete(entity);
        } catch (Exception e){
            //(3) exception 발생 시 id와 exception을 로깅한다.
            log.error("error deleting entity", entity.getId(), e);

            //(4) 컨트롤러로 exception을 날린다. 데이터베이스 내부 로직을 캡슐화하기 위해 e를 리턴하지 않고 새 exception 오브젝트를 리턴한다.
            throw new RuntimeException("error deleting entity" + entity.getId());
        }
        //(5) 해당 엔티티가 삭제된 Todo 리스트를 갖고와서 리턴한다.
        return retrieve(entity.getUserId());
    }

}
