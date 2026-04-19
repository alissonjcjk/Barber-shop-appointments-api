package br.com.barberdev.api.service.query;

import br.com.barberdev.api.entity.ClientEntity;

import java.util.List;

public interface IClientQueryService {
    ClientEntity findById(long id);
    List<ClientEntity> list();
    void verifyEmailAvailable(String email);
    void verifyEmailAvailable(long id, String email);
    void verifyPhoneAvailable(String phone);
    void verifyPhoneAvailable(long id, String phone);
}
