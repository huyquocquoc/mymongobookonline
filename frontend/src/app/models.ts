export interface Book {
  id?: string;
  isbn: string;
  title: string;
  author: string;
  price: number;
  stockQuantity: number;
  description?: string;
}

export interface AppUser {
  id?: string;
  email: string;
  fullName: string;
  shippingAddress: string;
}

export interface OrderItem {
  bookId: string;
  title: string;
  quantity: number;
  unitPrice: number;
}

export interface BookOrder {
  id?: string;
  userId: string;
  items: OrderItem[];
  totalAmount?: number;
  status?: 'CREATED' | 'CONFIRMED' | 'CANCELLED';
  createdAt?: string;
  updatedAt?: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

