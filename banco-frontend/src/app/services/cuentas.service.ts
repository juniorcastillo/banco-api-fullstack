import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Cuenta {
  id?: number;
  numeroCuenta: string;
  tipoCuenta: string;
  saldoInicial: number;
  saldoDisponible?: number;
  estado?: boolean;
  
    clienteId?: number;
    nombreCliente?: string;
    identificacion?: string;
    direccion?: string;
    telefono?: string;
}

@Injectable({
  providedIn: 'root'
})
export class CuentasService {

  private apiUrl = 'http://localhost:8080/cuentas';

  constructor(private http: HttpClient) {}

  getCuentas() {
    return this.http.get<Cuenta[]>(this.apiUrl);
  }

  crearCuenta(clienteId: number, cuenta: Partial<Cuenta>) {
    return this.http.post<Cuenta>(`${this.apiUrl}/cliente/${clienteId}`, cuenta);
  }

  actualizarCuenta(id: number, cuenta: Partial<Cuenta>) {
    return this.http.put<Cuenta>(`${this.apiUrl}/${id}`, cuenta);
  }

  eliminarCuenta(id: number) {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}