import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppUser, Book, BookOrder, Page } from './models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private readonly http: HttpClient) {}

  listBooks(query = '', page = 0, size = 10): Observable<Page<Book>> {
    let params = new HttpParams().set('page', String(page)).set('size', String(size));
    if (query.trim()) {
      params = params.set('q', query.trim());
    }
    return this.http.get<Page<Book>>('/api/books', { params });
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

