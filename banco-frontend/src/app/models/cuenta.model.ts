import { Cliente } from './cliente.model';

export interface Cuenta {
  id?: number;
  numeroCuenta: string;
  tipoCuenta: string;
  saldoInicial: number;
  saldoDisponible?: number;
  cliente?: Cliente;
}