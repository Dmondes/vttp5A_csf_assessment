import { firstValueFrom } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Menu, Order } from "./models";

@Injectable()
export class RestaurantService {
  private http = inject(HttpClient);
  private selectedItems: Menu [] = [];

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems(): Promise<Menu[]> {
    // This will get an array of strings from the API
    return firstValueFrom(this.http.get<any[]>("/api/menu")).then(
      (menuItems) => {
        // Transform the strings into Menu objects
        return menuItems.map((menuString, index) => {
          const menuObj = JSON.parse(menuString);
          return {
            id: menuObj.id,
            name: menuObj.name,
            price: parseFloat(menuObj.price),
            description: menuObj.description,
            quantity: 0,
          };
        });
      },
    );
  }
  // TODO: Task 3.
  setSelectedMenu(items: Menu[]): void {
    // Only keep items with quantity > 0
    this.selectedItems = items.filter(item => item.quantity > 0);
  }
  getSelectedMenu(): Menu[] {
    return this.selectedItems;
  }
  placeOrder(orderData: any): Promise<Order> {
    console.log(orderData);
    return firstValueFrom(
      this.http.post<Order>('/api/food_order', orderData)
    );
  }
}