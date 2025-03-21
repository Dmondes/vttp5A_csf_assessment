import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RestaurantService } from '../restaurant.service';
import { Menu } from '../models';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit {

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private restService = inject(RestaurantService);

  form!: FormGroup;
  selectedItems: Menu[] = [];
  totalCost: number = 0;

  // TODO: Task 3
  ngOnInit(): void {
    this.form = this.createForm();
    this.loadSelectedMenu();
  }

  private createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control<string>('', [Validators.required]),
      password: this.fb.control<string>('', [Validators.required])
    });
  }

  private loadSelectedMenu(): void {
    // Get the selected items from the service
    this.selectedItems = this.restService.getSelectedMenu();
    this.calculateTotal();
  }

  private calculateTotal(): void {
    this.totalCost = 0;
    for (let i = 0; i < this.selectedItems.length; i++) {
      const item = this.selectedItems[i];
      this.totalCost += (item.price * item.quantity);
    }
  }

  getItemSubtotal(item: Menu): number {
    return item.price * item.quantity;
  }

  startOver(): void {
    this.router.navigate(['/menu']);
  }

  confirmOrder(): void {
    if (this.form.invalid) {
      return;
    }

    const orderData = {
      username: this.form.get('username')?.value,
      password: this.form.get('password')?.value,
      items: this.selectedItems.map(item => ({
        id: item.id,
        quantity: item.quantity
      })),
      totalCost: this.totalCost
    };

    this.restService.placeOrder(orderData)
      .then(response => {
        this.router.navigate(['/confirmation'], {
          state: { orderDetails: response }
        });
      })
      .catch(error => {
        console.error('Order placement failed:', error);
      });
  }

}
