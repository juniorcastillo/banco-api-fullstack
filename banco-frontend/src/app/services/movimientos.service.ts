import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cuenta } from './cuentas.service';

export interface Movimiento {
  id?: number;
  fecha?: string;
  tipoMovimiento: 'CREDITO' | 'DEBITO';
  valor: number;
  saldo?: number;
 cuentaId?: number;
  nombreCliente?: string;
  numeroCuenta?: string;

  };


@Injectable({
  providedIn: 'root'
})
export class MovimientosService {

  private apiUrl = 'http://localhost:8080/movimientos';

  constructor(private http: HttpClient) {}

  getMovimientos(): Observable<Movimiento[]> {
    return this.http.get<Movimiento[]>(this.apiUrl);
  }

  crearMovimiento(cuentaId: number, movimiento: Partial<Movimiento>): Observable<Movimiento> {
    return this.http.post<Movimiento>(`${this.apiUrl}/cuenta/${cuentaId}`, movimiento);
  }

  eliminarMovimiento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}