// You may use this file to create any models
export interface Menu {
	id: string
    name: string
    price: number
    description: string
    quantity: number
}

export interface Order {
	orderId: string
    paymentId: string
    orderDate: Date
    total: number
    username: string
}