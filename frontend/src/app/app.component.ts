import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { Component, OnInit, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { ApiService } from './api.service';
import { ErrorService } from './error.service';
import { AppUser, Book, BookOrder, OrderItem, Page } from './models';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyPipe, DatePipe, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  readonly books = signal<Book[]>([]);
  readonly users = signal<AppUser[]>([]);
  readonly orders = signal<BookOrder[]>([]);
  readonly cart = signal<OrderItem[]>([]);
  readonly selectedUserId = signal('');
  readonly search = signal('');
  readonly message = signal('');
  readonly error = signal('');
  readonly page = signal(0);
  readonly totalPages = signal(1);
  readonly isLoading = signal(false);

  newBook: Book = {
    isbn: '',
    title: '',
    author: '',
    price: 0,
    stockQuantity: 0,
    description: ''
  };

  newUser: AppUser = {
    email: '',
    fullName: '',
    shippingAddress: ''
  };

  readonly cartTotal = computed(() =>
    this.cart().reduce((sum, item) => sum + item.quantity * item.unitPrice, 0)
  );

  constructor(private readonly api: ApiService, private readonly errorService: ErrorService) {}

  ngOnInit(): void {
    this.refreshAll();
  }

  refreshAll(): void {
    this.loadBooks(true);
    this.loadUsers();
    this.loadOrders();
  }

  loadBooks(reset = false): void {
    if (reset) {
      this.page.set(0);
      this.books.set([]);
      this.totalPages.set(1);
    }

    const nextPage = this.page();
    this.isLoading.set(true);
    this.api.listBooks(this.search(), nextPage, 10).subscribe({
      // if good then forward the return  to 
      next: (page) => {
        this.books.update((current) => [...current, ...page.content]);
        this.totalPages.set(page.totalPages);
        this.page.set(page.number + 1);
        this.isLoading.set(false);
      },
      // if not good then 
      error: () => {
        this.showError('Unable to load books');
        this.isLoading.set(false);
      }
    });
  }

  loadUsers(): void {
    this.api.listUsers().subscribe({
      next: (users) => this.users.set(users),
      error: () => this.showError('Unable to load users')
    });
  }

  loadMoreBooks(): void {
    if (this.isLoading() || this.page() >= this.totalPages()) {
      return;
    }
    this.loadBooks();
  }

  loadOrders(): void {
    this.api.listOrders().subscribe({
      next: (orders) => this.orders.set(orders),
      error: () => this.showError('Unable to load orders')
    });
  }

  createBook(): void {
    this.api.createBook(this.newBook).subscribe({
      next: () => {
        this.newBook = { isbn: '', title: '', author: '', price: 0, stockQuantity: 0, description: '' };
        this.loadBooks();
        this.showMessage('Book created');
      },
      error: () => this.showError('Unable to create book')
    });
  }

  createUser(): void {
    this.api.createUser(this.newUser).subscribe({
      next: (user) => {
        this.newUser = { email: '', fullName: '', shippingAddress: '' };
        this.selectedUserId.set(user.id ?? user.email);
        this.loadUsers();
        this.showMessage('User created');
      },
      error: () => this.showError('Unable to create user')
    });
  }

  addToCart(book: Book): void {
    const id = book.id ?? book.isbn;
    const next = [...this.cart()];
    const existing = next.find((item) => item.bookId === id);
    if (existing) {
      existing.quantity += 1;
    } else {
      next.push({ bookId: id, title: book.title, quantity: 1, unitPrice: book.price });
    }
    this.cart.set(next);
  }

  updateQuantity(item: OrderItem, quantity: number): void {
    const next = this.cart()
      .map((cartItem) => cartItem.bookId === item.bookId ? { ...cartItem, quantity } : cartItem)
      .filter((cartItem) => cartItem.quantity > 0);
    this.cart.set(next);
  }

  placeOrder(): void {
    if (!this.selectedUserId()) {
      this.showError('Select a user before placing an order');
      return;
    }
    if (this.cart().length === 0) {
      this.showError('Add at least one book to the order');
      return;
    }

    this.api.createOrder({ userId: this.selectedUserId(), items: this.cart() }).subscribe({
      next: () => {
        this.cart.set([]);
        this.loadOrders();
        this.showMessage('Order created and Kafka event published');
      },
      error: () => this.showError('Unable to create order')
    });
  }

  confirm(order: BookOrder): void {
    if (!order.id) {
      return;
    }
    this.api.confirmOrder(order.id).subscribe({
      next: () => this.loadOrders(),
      error: () => this.showError('Unable to confirm order')
    });
  }

  cancel(order: BookOrder): void {
    if (!order.id) {
      return;
    }
    this.api.cancelOrder(order.id).subscribe({
      next: () => this.loadOrders(),
      error: () => this.showError('Unable to cancel order')
    });
  }

  private showMessage(message: string): void {
    this.message.set(message);
    this.error.set('');
  }

  private showError(message: string): void {
    this.error.set(message);
    this.message.set('');
  }

  criticalError(message: string, statusCode?: number): void {
    this.errorService.redirectToError({
      message,
      statusCode
    });
  }

}