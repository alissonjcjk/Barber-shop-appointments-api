package br.com.barberdev.api.service.impl;

import br.com.barberdev.api.entity.ClientEntity;
import br.com.barberdev.api.repository.IClientRepository;
import br.com.barberdev.api.service.IClientService;
import br.com.barberdev.api.service.query.IClientQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ClientService implements IClientService {

    private final IClientRepository repository;
    private final IClientQueryService queryService;

    @Override
    @Transactional
    public ClientEntity save(final ClientEntity entity) {
        queryService.verifyEmailAvailable(entity.getEmail());
        queryService.verifyPhoneAvailable(entity.getPhone());
        return repository.save(entity);
    }

    @Override
    @Transactional
    public ClientEntity update(final ClientEntity entity) {
        queryService.verifyEmailAvailable(entity.getId(), entity.getEmail());
        queryService.verifyPhoneAvailable(entity.getId(), entity.getPhone());

        var stored = queryService.findById(entity.getId());
        stored.setName(entity.getName());
        stored.setPhone(entity.getPhone());
        stored.setEmail(entity.getEmail());
        return repository.save(stored);
    }

    @Override
    @Transactional
    public void delete(final long id) {
        queryService.findById(id);
        repository.deleteById(id);
    }
}
