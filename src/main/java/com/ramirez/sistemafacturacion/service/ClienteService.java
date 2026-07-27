package com.ramirez.sistemafacturacion.service;

import com.ramirez.sistemafacturacion.domain.ClienteDomain;
import com.ramirez.sistemafacturacion.mapper.ClienteMapper;
import com.ramirez.sistemafacturacion.model.Cliente;
import com.ramirez.sistemafacturacion.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Transactional(readOnly = true)
    public List<ClienteDomain> findAll() {
        return StreamSupport.stream(clienteRepository.findAll().spliterator(), false)
                .map(clienteMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteDomain findById(Integer id) {
        return getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    @Transactional(readOnly = true)
    public Optional<ClienteDomain> getById(Integer id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toDomain);
    }

    @Transactional
    public ClienteDomain save(ClienteDomain clienteDomain) {
        Cliente cliente = clienteMapper.toEntity(clienteDomain);
        return clienteMapper.toDomain(clienteRepository.save(cliente));
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!clienteRepository.existsById(id)) {
            return false;
        }
        clienteRepository.deleteById(id);
        return true;
    }
}
