package com.aburakkontas.manga_payment.infrastructure.repositories;

import com.aburakkontas.manga_payment.domain.dtos.InitiliazeCheckoutFormDTO;
import com.aburakkontas.manga_payment.domain.entities.Item;
import com.aburakkontas.manga_payment.domain.repositories.IyzicoRepository;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class IyzicoRepositoryImpl implements IyzicoRepository {

    private final Options options = new Options();

    @Autowired
    public IyzicoRepositoryImpl(Environment env) {
        options.setApiKey(env.getProperty("iyzico.apiKey"));
        options.setSecretKey(env.getProperty("iyzico.secretKey"));
        options.setBaseUrl(env.getProperty("iyzico.baseUrl"));
    }

    public CheckoutFormInitialize initializeCheck(InitiliazeCheckoutFormDTO initiliazeCheckoutFormDTO) {
        var paidPrice = initiliazeCheckoutFormDTO.getItems().stream().reduce(0.0, (subtotal, item) -> item.getPrice(), Double::sum);

        var request = createCheckoutFormInitializeRequest(initiliazeCheckoutFormDTO, paidPrice);
        var buyer = createBuyer(initiliazeCheckoutFormDTO);
        var address = createAddress(initiliazeCheckoutFormDTO);
        var basketItems = initiliazeCheckoutFormDTO.getItems().stream().map(this::createBasketItem).toList();

        request.setBuyer(buyer);
        request.setShippingAddress(address);
        request.setBillingAddress(address);
        request.setBasketItems(basketItems);

        return CheckoutFormInitialize.create(request, options);

    }

    private Buyer createBuyer(InitiliazeCheckoutFormDTO initiliazeCheckoutFormDTO) {
        Buyer buyer = new Buyer();
        buyer.setId(initiliazeCheckoutFormDTO.getUserId().toString());
        buyer.setName(initiliazeCheckoutFormDTO.getFirstName());
        buyer.setSurname(initiliazeCheckoutFormDTO.getLastName());
        buyer.setEmail(initiliazeCheckoutFormDTO.getEmail());
        buyer.setRegistrationAddress("-");
        buyer.setCity("-");
        buyer.setCountry("-");

        return buyer;
    }

    private CreateCheckoutFormInitializeRequest createCheckoutFormInitializeRequest(InitiliazeCheckoutFormDTO initiliazeCheckoutFormDTO, Double price) {

        CreateCheckoutFormInitializeRequest request = new CreateCheckoutFormInitializeRequest();
        request.setLocale(Locale.TR.getValue());
        request.setConversationId(initiliazeCheckoutFormDTO.getUserId().toString());
        request.setPrice(new BigDecimal(price * 100 / 20));
        request.setPaidPrice(new BigDecimal(price));
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
