import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppUser, Book, BookOrder } from './models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private readonly http: HttpClient) {}

  listBooks(query = ''): Observable<Book[]> {
    const url = query.trim() ? `/api/books?q=${encodeURIComponent(query.trim())}` : '/api/books';
    return this.http.get<Book[]>(url);
  }

  createBook(book: Book): Observable<Book> {
    return this.http.post<Book>('/api/books', book);
  }

  listUsers(): Observable<AppUser[]> {
    return this.http.get<AppUser[]>('/api/users');
  }

  createUser(user: AppUser): Observable<AppUser> {
    return this.http.post<AppUser>('/api/users', user);
  }

  listOrders(): Observable<BookOrder[]> {
    return this.http.get<BookOrder[]>('/api/orders');
  }

  createOrder(order: BookOrder): Observable<BookOrder> {
    return this.http.post<BookOrder>('/api/orders', order);
  }

  confirmOrder(id: string): Observable<BookOrder> {
    return this.http.post<BookOrder>(`/api/orders/${id}/confirm`, {});
  }

  cancelOrder(id: string): Observable<BookOrder> {
    return this.http.post<BookOrder>(`/api/orders/${id}/cancel`, {});
  }
}

