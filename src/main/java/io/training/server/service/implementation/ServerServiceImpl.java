package io.training.server.service.implementation;

import io.training.server.enumeration.Status;
import io.training.server.model.Server;
import io.training.server.repo.ServerRepo;
import io.training.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

@RequiredArgsConstructor // Lombok te crée un constructeur avec le field dedans
@Service
@Transactional
@Slf4j // pour afficher dans la console
public class ServerServiceImpl implements ServerService {

    private final ServerRepo serverRepo;


    @Override
    public Server create(Server server) {
        log.info("Saving a new server: {}", server.getName());
        server.setImageUrl(setServerimageUrl());
        return serverRepo.save(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = serverRepo.findByIpAddress(ipAddress);

        // on essaye de ping ce serveur
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? Status.SERVER_UP : Status.SERVER_DOWN);
        serverRepo.save(server);

        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers");
        return serverRepo.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching a server by id: {}", id);
        return serverRepo.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Updating a server: {}", server.getName());
        return serverRepo.save(server); // detecte si l'ID existe, il l'écrase par le nouveau. Sinon il crée
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting a server by id: {}", id);
        serverRepo.deleteById(id); // ça renvoit true si cette ligne passe, sinon ça n'atteint pas true
        return Boolean.TRUE;
    }

    private String setServerimageUrl() {
        String[] imagesNames = { "server1.png", "server2.png", "server3.png", "server4.png"};
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/server/image/" + imagesNames[new Random().nextInt(4)])
                .toUriString();
    }
}
