package com.vocacional.orientacionvocacional.Mapper;

import com.vocacional.orientacionvocacional.dto.PurchaseCreateDTO;
import com.vocacional.orientacionvocacional.dto.PurchaseDTO;
import com.vocacional.orientacionvocacional.dto.PurchaseItemCreateDTO;
import com.vocacional.orientacionvocacional.dto.PurchaseItemDTO;
import com.vocacional.orientacionvocacional.model.entity.Plan;
import com.vocacional.orientacionvocacional.model.entity.Purchase;
import com.vocacional.orientacionvocacional.model.entity.PurchaseItem;
import com.vocacional.orientacionvocacional.model.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {
    private final ModelMapper modelMapper;

    public PurchaseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private PurchaseItem toPurchaseItemEntity(PurchaseItemCreateDTO purchaseItemDTO) {
        PurchaseItem item = modelMapper.map(purchaseItemDTO, PurchaseItem.class);
        Plan plan = new Plan();
        plan.setId(purchaseItemDTO.getPlanId());
        item.setPlan(plan);
        return item;
    }

    private PurchaseItemDTO toPurchaseItemDTO(PurchaseItem purchaseItem) {
        PurchaseItemDTO purchaseItemDTO = modelMapper.map(purchaseItem, PurchaseItemDTO.class);
        purchaseItemDTO.setPlanName(purchaseItem.getPlan().getName());
        return purchaseItemDTO;
    }


   public Purchase toPurchaseCreateDTO(PurchaseCreateDTO purchaseCreateDTO){
        Purchase purchase = modelMapper.map(purchaseCreateDTO, Purchase.class);
       User user = new User();
       user.setId(purchaseCreateDTO.getUserId());
       purchase.setUser(user);

       purchase.setItems(purchaseCreateDTO.getItems().stream()
               .map(this::toPurchaseItemEntity)
               .toList());

       return purchase;
   }

   public PurchaseDTO toPurchaseDTO(Purchase purchase){
       PurchaseDTO purchaseDTO = modelMapper.map(purchase, PurchaseDTO.class);
       purchaseDTO.setUserId(purchase.getUser().getId());
       purchaseDTO.setItems(purchase.getItems().stream()
               .map(this::toPurchaseItemDTO)
               .toList());
       return purchaseDTO;
   }
}
