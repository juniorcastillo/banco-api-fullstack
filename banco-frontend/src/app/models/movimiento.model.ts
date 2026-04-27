import { Cuenta } from './cuenta.model';

export interface Movimiento {
  id?: number;
  fecha?: string;
  tipoMovimiento: string;
  valor: number;
  saldo?: number;
  cuenta?: Cuenta;
}