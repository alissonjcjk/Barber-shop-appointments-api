package br.com.barberdev.api.service;

import br.com.barberdev.api.entity.ClientEntity;

public interface IClientService {
    ClientEntity save(ClientEntity entity);
    ClientEntity update(ClientEntity entity);
    void delete(long id);
}
