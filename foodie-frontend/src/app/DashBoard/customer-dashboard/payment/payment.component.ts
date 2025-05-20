import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent {
  CustomerId: number = 0;
  restaurantId: number = 0;
  cart: any[] = [];
  selectedPayment: string = '';
  upiId: string = '';
  cardNumber: string = '';
  expiryDate: string = '';
  cvv: string = '';
  method: string = '';
  GrandTotal: string | null = '';
  
  httpHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient, private router: Router) {
    const nav = this.router.getCurrentNavigation();
    const state = nav?.extras?.state as { cart: any[] };
    this.cart = state?.cart || [];
  }

  ngOnInit(): void {
    this.restaurantId = Number(localStorage.getItem('restaurantId'));
    this.CustomerId = Number(localStorage.getItem('userId'));
    this.GrandTotal = localStorage.getItem('grandTotal');
  }

  async confirmPayment() {
    if (!this.selectedPayment) {
      alert('Please select a payment method!');
      return;
    }

    if (this.selectedPayment === 'UPI') {
      if (!this.upiId) {
        alert('Please enter your UPI ID.');
        return;
      }
      console.log('Paying with UPI ID:', this.upiId);
    } else if (this.selectedPayment === 'CreditCard' || this.selectedPayment === 'DebitCard') {
      if (!this.cardNumber || !this.expiryDate || !this.cvv) {
        alert('Please fill all card details.');
        return;
      }
      console.log('Paying with card:', this.cardNumber);
    } else {
      console.log('Cash on Delivery selected');
    }

    try {
      const orderResponse = await lastValueFrom(this.createOrder());
      const orderID = orderResponse?.body?.id;
      console.log('Order ID:', orderID);

      const paymentData: {
        orderID: any;
        customerID: number;
        restaurantID: number;
        typeOfPayment: string;
        amount: string | null;
        upiId?: string;
        cardNumber?: string;
        expiryDate?: string;
        cvv?: string;
      } = {
        orderID: orderID, 
        customerID: this.CustomerId,
        restaurantID: this.restaurantId,
        typeOfPayment: this.selectedPayment,
        amount: this.GrandTotal,
      };
      if(this.selectedPayment === 'UPI') {
        paymentData['upiId'] = this.upiId;
      }
      else if(this.selectedPayment === 'CreditCard' || this.selectedPayment === 'DebitCard') {
        paymentData['cardNumber'] = this.cardNumber;
        paymentData['expiryDate'] = this.expiryDate;
        paymentData['cvv'] = this.cvv;
      }

      console.log('Payment data:', paymentData);

      const sanitizedPaymentMethod = encodeURIComponent(this.selectedPayment.trim());

      const response = await lastValueFrom(
        this.http.post<any>(
          `http://localhost:8080/order/pay/${sanitizedPaymentMethod}`,JSON.stringify(paymentData),{
        headers: this.httpHeaders,
        observe: 'response'
          }
        )
      );

      console.log('Payment successful!', response);
      alert(`Payment of ₹${this.GrandTotal} successful with ${this.selectedPayment}! 🎉`);
      localStorage.removeItem(`cart+${this.restaurantId}`);
      this.router.navigate(['/customer-dashboard']);

    } catch (error) {
      console.error('Payment failed!', error);
      alert('Payment failed. Please try again.');
    }
  }

  createOrder() {
    const orderData = {
      customerId: this.CustomerId,
      restaurantId: this.restaurantId,
      paymentStatus: 'PENDING',
      totalPrice: this.GrandTotal,
      deliveryAddress: localStorage.getItem('customerAddress'),
      orderedItem: this.cart.map(item => ({
        itemId: item.itemId,
        itemName: item.name,
        quantity: item.quantity,
        itemPrice: item.price,
      })),
    };
    console.log('Order data:', orderData);

    return this.http.post<any>(`http://localhost:8080/order/add`, orderData, {
      headers: this.httpHeaders,
      observe: 'response'
    });
  }
}