package com.project.auth_service.demo.services;

import com.project.auth_service.demo.models.Animal;
import com.project.auth_service.demo.repositories.AnimalRepositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepositorie animalRepository;

    public List<Animal> findAll() {
        return animalRepository.findAll();
    }

    public Optional<Animal> findById(Long id) {
        return animalRepository.findById(id);
    }

    public Animal save(Animal animal) {
        return animalRepository.save(animal);
    }

    public Animal update(Long id, Animal updatedAnimal) {
        return animalRepository.findById(id).map(animal -> {
            animal.setNome(updatedAnimal.getNome());
            animal.setEspecie(updatedAnimal.getEspecie());
            return animalRepository.save(animal);
        }).orElseThrow(() -> new RuntimeException("Animal não encontrado com o ID: " + id));
    }

    public void delete(Long id) {
        if (!animalRepository.existsById(id)) {
            throw new RuntimeException("Animal não encontrado com o ID: " + id);
        }
        animalRepository.deleteById(id);
    }
}