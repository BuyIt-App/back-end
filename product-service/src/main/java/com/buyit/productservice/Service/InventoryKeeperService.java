package com.buyit.productservice.Service;

import com.buyit.productservice.dto.InventoryKeeperDto;
import com.buyit.productservice.model.InventoryKeeper;
import com.buyit.productservice.repository.InventoryKeeperRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class InventoryKeeperService {
    private final InventoryKeeperRepo inventoryKeeperRepo;


    public List<InventoryKeeperDto> getAllInventoryKeepers() {
        List<InventoryKeeper> iks = inventoryKeeperRepo.findAll();
        List<InventoryKeeperDto> ikDtos = new ArrayList<>();
        for(InventoryKeeper i : iks){
            InventoryKeeperDto inventoryKeeperDto = mapIktoIkDto(i);
            ikDtos.add(inventoryKeeperDto);
        }

        return ikDtos;
    }

    public InventoryKeeperDto getByIkId(long ikId){
        InventoryKeeper inventoryKeeper = inventoryKeeperRepo.findById(ikId).orElse(null);
        return mapIktoIkDto(inventoryKeeper);
    }


    public InventoryKeeperDto mapIktoIkDto(InventoryKeeper inventoryKeeper){
        InventoryKeeperDto inventoryKeeperDto = new InventoryKeeperDto();
        inventoryKeeperDto.setInventoryKeeperId(inventoryKeeper.getInventoryKeeperId());
        inventoryKeeperDto.setAddress(inventoryKeeper.getAddress());
        inventoryKeeperDto.setEmailId(inventoryKeeper.getEmailId());
        inventoryKeeperDto.setFirstName(inventoryKeeper.getFirstName());
        inventoryKeeperDto.setLastName(inventoryKeeper.getLastName());
        inventoryKeeperDto.setPhoneNumber(inventoryKeeper.getPhoneNumber());
        return inventoryKeeperDto;
    }

    public InventoryKeeperDto updateIk(InventoryKeeperDto inventoryKeeperDto, long ikId){
        InventoryKeeper inventoryKeeper = inventoryKeeperRepo.findById(ikId).get();
        inventoryKeeperDto.setInventoryKeeperId(ikId);
        inventoryKeeper.setAddress(inventoryKeeperDto.getAddress());
        inventoryKeeper.setEmailId(inventoryKeeperDto.getEmailId());
        inventoryKeeper.setFirstName(inventoryKeeperDto.getFirstName());
        inventoryKeeper.setLastName(inventoryKeeperDto.getLastName());
        inventoryKeeper.setPhoneNumber(inventoryKeeperDto.getPhoneNumber());
        inventoryKeeperRepo.save(inventoryKeeper);

        return  inventoryKeeperDto;
    }
}
