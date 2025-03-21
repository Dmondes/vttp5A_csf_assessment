import { Component, inject, OnInit } from "@angular/core";
import { RestaurantService } from "../restaurant.service";
import { Menu, Order } from "../models";
import { FormBuilder, FormGroup } from "@angular/forms";
import { Router } from "@angular/router";

@Component({
  selector: "app-menu",
  standalone: false,
  templateUrl: "./menu.component.html",
  styleUrl: "./menu.component.css",
})
export class MenuComponent implements OnInit {
  private fb = inject(FormBuilder);
  private restService = inject(RestaurantService);
  private router = inject(Router);

  menuList: Menu[] = [];
  form!: FormGroup;
  order: Order = {
    orderId: "",
    paymentId: "",
    orderDate: new Date(),
    total: 0,
    username: "",
  };
  totalItems = 0;
  // TODO: Task 2

  ngOnInit(): void {
    this.form = this.createForm();
    this.getMenuDetails();
  }

  removeQty(menu: Menu) {
    //remove quantity of menu item
    if (menu.quantity > 0) {
      menu.quantity--;
      this.updateOrderTotal();
      
    }
    this.updateTotalMenu();
  }

  addQty(menu: Menu) {
    //add quantity of menu item
    menu.quantity = (menu.quantity || 0) + 1;
    this.updateOrderTotal();
    this.updateTotalMenu();
  }

  updateTotalMenu() {
    let total = 0;
    for (let i = 0; i < this.menuList.length; i++) {
      const menu = this.menuList[i];
      if (menu.quantity && menu.quantity > 0) {
        total++;
      }
    }
    this.totalItems = total;
  }

  updateOrderTotal() {
    let total = 0;

    for (let i = 0; i < this.menuList.length; i++) {
      const menu = this.menuList[i];
      const quantity = menu.quantity || 0;
      total += menu.price * quantity;
    }
    // 2 dp
    this.order.total = parseFloat(total.toFixed(2));
  }

  private createForm(): FormGroup {
    return this.fb.group({});
  }

  getMenuDetails() {
    this.restService.getMenuItems()
      .then((response) => {
        this.menuList = response;
        console.log(" menu list:", this.menuList);
      })
      .catch((err) => {
        console.error("Error fetching menu:", err);
      });
  }
  placeOrder() {
    this.restService.setSelectedMenu(this.menuList);
      this.router.navigate(["/place-order"]);
  }
}
