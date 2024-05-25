package com.aburakkontas.manga_payment.infrastructure.repositories;

import com.aburakkontas.manga_payment.domain.dtos.InitiliazeCheckoutFormDTO;
import com.aburakkontas.manga_payment.domain.dtos.RetrieveCheckoutFormDTO;
import com.aburakkontas.manga_payment.domain.dtos.RetrieveCheckoutFormResultDTO;
import com.aburakkontas.manga_payment.domain.entities.item.Item;
import com.aburakkontas.manga_payment.domain.repositories.IyzicoRepository;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import com.iyzipay.request.RetrieveCheckoutFormRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class IyzicoRepositoryImpl implements IyzicoRepository {

    private final Options options = new Options();

    @Autowired
    public IyzicoRepositoryImpl(Environment env) {
        options.setApiKey(env.getProperty("iyzico.apiKey"));
        options.setSecretKey(env.getProperty("iyzico.secretKey"));
        options.setBaseUrl(env.getProperty("iyzico.baseUrl"));
    }

    public CheckoutFormInitialize initializeCheckout(InitiliazeCheckoutFormDTO initiliazeCheckoutFormDTO) {
        var pricesSum = initiliazeCheckoutFormDTO.getItems().stream()
                .mapToDouble(Item::getPrice)
                .sum();

        var price = new BigDecimal(pricesSum).setScale(2, RoundingMode.HALF_UP);

        var request = createCheckoutFormInitializeRequest(initiliazeCheckoutFormDTO, price);
        var buyer = createBuyer(initiliazeCheckoutFormDTO);
        var address = createAddress(initiliazeCheckoutFormDTO);
        var basketItems = initiliazeCheckoutFormDTO.getItems().stream().map(this::createBasketItem).toList();

        request.setBuyer(buyer);
        request.setShippingAddress(address);
        request.setBillingAddress(address);
        request.setBasketItems(basketItems);

        return CheckoutFormInitialize.create(request, options);

    }

    @Override
    public RetrieveCheckoutFormResultDTO retrieveCheckoutForm(RetrieveCheckoutFormDTO retrieveCheckoutFormDTO) {
        var request = new RetrieveCheckoutFormRequest();
        request.setConversationId(retrieveCheckoutFormDTO.getConversationId());
        request.setToken(retrieveCheckoutFormDTO.getToken());

        var response = CheckoutForm.retrieve(request, options);

        var itemIds = new ArrayList<UUID>();
        response.getPaymentItems().forEach(paymentItem -> {
            itemIds.add(UUID.fromString(paymentItem.getItemId()));
        });

        var result = new RetrieveCheckoutFormResultDTO(
                response.getStatus().equals("success"),
                response.getPrice().doubleValue(),
                response.getPaidPrice().doubleValue(),
                response.getPaymentId(),
                response.getConversationId(),
                response.getToken(),
                response.getCardType(),
                response.getCardAssociation(),
                response.getCardFamily(),
                response.getLastFourDigits(),
                itemIds
        );

        return result;
    }

    private Buyer createBuyer(InitiliazeCheckoutFormDTO initiliazeCheckoutFormDTO) {
        Buyer buyer = new Buyer();
        buyer.setId(initiliazeCheckoutFormDTO.getUserId().toString());
        buyer.setName(initiliazeCheckoutFormDTO.getFirstName());
        buyer.setSurname(initiliazeCheckoutFormDTO.getLastName());
        buyer.setEmail(initiliazeCheckoutFormDTO.getEmail());
        buyer.setIdentityNumber("00000000000");
        buyer.setRegistrationAddress("-");
        buyer.setCity("-");
        buyer.setCountry("-");

        return buyer;
    }

    private CreateCheckoutFormInitializeRequest createCheckoutFormInitializeRequest(InitiliazeCheckoutFormDTO initiliazeCheckoutFormDTO, BigDecimal price) {

        CreateCheckoutFormInitializeRequest request = new CreateCheckoutFormInitializeRequest();
        request.setLocale(Locale.TR.getValue());
        request.setConversationId(initiliazeCheckoutFormDTO.getUserId().toString());
        request.setPrice(price);
        request.setPaidPrice(price);
        request.setCurrency(Currency.TRY.name());
        request.setCallbackUrl(initiliazeCheckoutFormDTO.getCallbackUrl());

        return request;
    }

    private Address createAddress(InitiliazeCheckoutFormDTO initiliazeCheckoutFormDTO) {
        var address = new Address();
        address.setContactName(initiliazeCheckoutFormDTO.getFirstName() + " " + initiliazeCheckoutFormDTO.getLastName());
        address.setCity("-");
        address.setCountry("-");
        address.setAddress("-");
        address.setZipCode("-");
        return address;
    }

    private BasketItem createBasketItem(Item item) {
        var basketItem = new BasketItem();
        basketItem.setId(item.getId().toString());
        basketItem.setName(item.getName());
        basketItem.setCategory1(item.getCategory());
        basketItem.setItemType(BasketItemType.VIRTUAL.name());
        basketItem.setPrice(BigDecimal.valueOf(item.getPrice()));
        return basketItem;
    }

}
