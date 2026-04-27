import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'clientes', pathMatch: 'full' },

  {
    path: 'clientes',
    loadComponent: () =>
      import('./pages/clientes/clientes').then(m => m.Clientes)
  },
  {
    path: 'cuentas',
    loadComponent: () =>
      import('./pages/cuentas/cuentas').then(m => m.Cuentas)
  },
  {
    path: 'movimientos',
    loadComponent: () =>
      import('./pages/movimientos/movimientos').then(m => m.Movimientos)
  },
  {
    path: 'reportes',
    loadComponent: () =>
      import('./pages/reportes/reportes').then(m => m.Reportes)
  },

  { path: '**', redirectTo: 'clientes' }
];