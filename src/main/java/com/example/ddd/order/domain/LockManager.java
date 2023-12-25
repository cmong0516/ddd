package com.example.ddd.order.domain;

import javax.xml.crypto.Data;
import org.springframework.security.authentication.LockedException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

public interface LockManager {
    // 8.4 오프라인 선점 잠금

    // 한 공유 문서를 두 사용자가 함께 사용중이라고 할때 한 사용자가 수정하고 있을때 충돌을 방지하는 방법중 하나는 다른사용자가 접근하지 못하게 하는것이다.
    // 첫 트랜잭션에서 오프라인 잠금을 선점하고 마지막 트랜잭션에서 잠금을 해제.
    // 만약 마지막 트랜잭션 전에 프로그램을 종료한다면 영원히 다른 사용자가 접근할수 없으므로 잠금 유효시간을 둔다.

    LockId tryLock(String type, String id) throws LockException;

    void checkLock(LockId lockId) throws LockeException;

    void releaseLock(LockId lockId) throws LockException;

    void extendLockExpiration(LockId lockId, long inc) throws LockException;

//    서비스 내부에서 잠금 ID를 리턴한다.
//    public DataAndLockId getDataWithLock(Long id){
//
//        // 오프라인 선점 잠금 시도
//        LockId lockId = lockManager.tryLock("data", id);
//
//        // 기능 실행
//        Data data = someDao.select(id);
//
//        return new DataAndLock(data, lockId);
//    }


//    컨트롤러
//    @RequestMapping("/some/edit/{id}")
//    public String editForm(@PathVariable("id") Long id, ModelMap modelMap){
//        DataAndLockId dataAndLockId = dataService.getDataWithLock(id);
//        modelMap.addAttribute("data", dataAndLockId.getData());
//
//        modelMap.addAttribute("lockId", dataAndLockId.getLockId());
//
//        return "editForm";
//    }

}
