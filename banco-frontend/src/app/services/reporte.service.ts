import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Reporte {
  fecha: string;
  cliente: string;
  numeroCuenta: string;
  tipoCuenta: string;
  saldoInicial: number;
  estado: boolean;
  movimiento: number;
  saldoDisponible: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReportesService {

  private apiUrl = 'http://localhost:8080/reportes';

  constructor(private http: HttpClient) {}

  getReporte(clienteId: number, fechaInicio: string, fechaFin: string): Observable<Reporte[]> {
    return this.http.get<Reporte[]>(
      `${this.apiUrl}?clienteId=${clienteId}&fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`
    );
  }

  getReportePdf(clienteId: number, fechaInicio: string, fechaFin: string) {
    return this.http.get<any>(
      `${this.apiUrl}/pdf?clienteId=${clienteId}&fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`
    );
  }
}