package com.aburakkontas.manga_payment.domain.repositories;

import com.aburakkontas.manga_payment.domain.dtos.InitiliazeCheckoutFormDTO;
import com.iyzipay.model.CheckoutFormInitialize;

public interface IyzicoRepository {
    CheckoutFormInitialize initializeCheckout(InitiliazeCheckoutFormDTO initiliazeCheckoutFormDTO);
}