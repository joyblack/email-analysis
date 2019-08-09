package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.data.request.account.warning.SetStateData;
import com.sunrun.emailanalysis.data.request.account.warning.WarningAccountData;
import com.sunrun.emailanalysis.data.request.account.warning.WarningAccountListData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.dictionary.common.ResultDictionary;
import com.sunrun.emailanalysis.dictionary.database.CommonDictionary;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.joy.result.Notice;
import com.sunrun.emailanalysis.mapper.WarningAccountMapper;
import com.sunrun.emailanalysis.po.WarningAccount;
import com.sunrun.emailanalysis.tool.uuid.JoyUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class WarningAccountService {

    @Autowired
    private WarningAccountMapper warningAccountMapper;

    public EAResult add(WarningAccountData data) {
        try{
            String address = data.getAddress();
            if(address == null){
                throw new EAException("账户不能为空", Notice.REQUEST_PARAMETER_IS_ERROR);
            }
            WarningAccount account = new WarningAccount();
            account.setAddress(address);
            List<WarningAccount> select = warningAccountMapper.select(account);

            if(select != null && select.size() > 0){
                throw new EAException("已存在此账户，无需添加", Notice.ADDRESS_IS_ALREADY_EXIST);
            }
            Date now = new Date();
            Long accountId = JoyUUID.getUUId();
            account.setAccountId(accountId);
            account.setDomainName("");
            account.setNickName("");
            account.setState(0);
            account.setCreateTime(now);
            account.setEditTime(now);
            warningAccountMapper.insertSelective(account);

            return EAResult.buildSuccessResult(warningAccountMapper.getWarningAccount(accountId));
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }

    }

    public EAResult edit(WarningAccountData data) {
        try{
            String address = data.getAddress();
            Long accountId = data.getId();
            if(accountId == null || accountId.equals(0L)){
                throw new EAException("请设置有效的账户ID", Notice.REQUEST_PARAMETER_IS_ERROR);
            }
            if(address == null){
                throw new EAException("账户不能为空", Notice.REQUEST_PARAMETER_IS_ERROR);
            }
            WarningAccount account = new WarningAccount();
            account.setAccountId(accountId);
            account.setAddress(address);
            account.setEditTime(new Date());
            warningAccountMapper.updateByPrimaryKeySelective(account);

            return EAResult.buildSuccessResult(warningAccountMapper.getWarningAccount(accountId));
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(Notice.EXECUTE_IS_FAILED);
        }

    }

    public EAResult delete(IDData data) {
        try{
            Long accountId = data.getId();
            if(accountId == null || accountId.equals(0L)){
                throw new EAException("请设置有效的账户ID", Notice.REQUEST_PARAMETER_IS_ERROR);
            }
            warningAccountMapper.deleteByPrimaryKey(accountId);
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(Notice.EXECUTE_IS_FAILED);
        }
        return EAResult.buildSuccessResult();
    }



    public HashMap<String, Object> getAccountListCount(WarningAccountListData data) {
        // Get attach information.
        try {
            HashMap<String, Object> result = new HashMap<>();
            // Get total.
            Long total = warningAccountMapper.getAccountListCount(data);
            result.put(ResultDictionary.TOTAL, total == null? 0L : total);
            return result;
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }

    }

    public List<HashMap<String, Object>> getAccountList(WarningAccountListData data) {
        // Get attach information.
        try {
            return warningAccountMapper.getAccountList(data);
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }

    }

    public EAResult setState(SetStateData data) {
        try{
            Integer state = data.getState();
            if(state == null || ! (state.equals(CommonDictionary.STATE_DISABLE) || state.equals(CommonDictionary.STATE_ENABLE))){
                throw new EAException("请设置有效的状态取值（0或1）", Notice.REQUEST_PARAMETER_IS_ERROR);
            }
            List<Long> ids = data.getIds();
            if(ids == null || ids.size() == 0){
                throw new EAException("请至少提供一个告警账户ID", Notice.REQUEST_PARAMETER_IS_ERROR);
            }
            warningAccountMapper.setState(state, ids);
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(Notice.EXECUTE_IS_FAILED);
        }
        return EAResult.buildSuccessResult();
    }
}
