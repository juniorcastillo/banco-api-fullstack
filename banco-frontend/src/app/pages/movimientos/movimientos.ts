import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Movimiento, MovimientosService } from '../../services/movimientos.service';
import { CuentasService, Cuenta } from '../../services/cuentas.service';

@Component({
  selector: 'app-movimientos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './movimientos.html',
  styleUrl: './movimientos.css'
})
export class Movimientos implements OnInit {

  movimientos = signal<Movimiento[]>([]);
  cuentas = signal<Cuenta[]>([]);
  busqueda = signal('');

  mostrarFormulario = false;

  mensajeError = signal('');
  mensajeExito = signal('');

  movimientoForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private movimientosService: MovimientosService,
    private cuentasService: CuentasService
  ) {
    this.movimientoForm = this.fb.group({
      cuentaId: [null, [Validators.required]],
      tipoMovimiento: ['', [Validators.required]],
      valor: [null, [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit(): void {
    this.cargarMovimientos();
    this.cargarCuentas();
  }

  cargarMovimientos(): void {
    this.movimientosService.getMovimientos().subscribe({
      next: (data) => this.movimientos.set(data),
      error: (err: any) => {
        console.error('Error cargando movimientos:', err);
        this.mensajeError.set('Error al cargar los movimientos');
      }
    });
  }

  cargarCuentas(): void {
    this.cuentasService.getCuentas().subscribe({
      next: (data) => this.cuentas.set(data),
      error: (err: any) => {
        console.error('Error cargando cuentas:', err);
        this.mensajeError.set('Error al cargar las cuentas');
      }
    });
  }

  abrirNuevo(): void {
    this.limpiarMensajes();
    this.mostrarFormulario = true;

    this.movimientoForm.reset({
      cuentaId: null,
      tipoMovimiento: '',
      valor: null
    });
  }

  crearMovimiento(): void {
    this.limpiarMensajes();

    if (this.movimientoForm.invalid) {
      this.movimientoForm.markAllAsTouched();
      this.mensajeError.set('Revisa los campos del formulario');
      return;
    }

    const formValue = this.movimientoForm.getRawValue();

    const movimientoEnviar = {
      tipoMovimiento: formValue.tipoMovimiento,
      valor: Number(formValue.valor)
    };

    this.movimientosService.crearMovimiento(
      Number(formValue.cuentaId),
      movimientoEnviar
    ).subscribe({
      next: () => {
        this.cargarMovimientos();
        this.cancelarFormulario(false);
        this.mensajeExito.set('Movimiento registrado correctamente');
      },
     error: (err: any) => {
  console.error('Error creando movimiento:', err);

  const mensajeBackend = err.error?.detalles?.message || err.error?.detalles || '';

 /*  if (String(mensajeBackend).includes('El valor debe ser mayor que cero')) {
    this.mensajeError.set('El valor debe ser mayor que cero');
    return;
  }

  if (String(mensajeBackend).includes('Saldo no disponible')) {
    this.mensajeError.set('Saldo no disponible');
    return;
  }

  if (String(mensajeBackend).includes('Cupo diario Excedido')) {
    this.mensajeError.set('Cupo diario Excedido');
    return;
  } */

    if(mensajeBackend ){
 this.mensajeError.set(mensajeBackend);
 return;
    }
    this.mensajeError.set('Error al registrar movimiento');
}
    });
  }

  eliminarMovimiento(id: number): void {
    this.limpiarMensajes();

    if (!confirm('¿Está seguro de eliminar este movimiento?')) {
      return;
    }

    this.movimientosService.eliminarMovimiento(id).subscribe({
      next: () => {
        this.cargarMovimientos();
        this.mensajeExito.set('Movimiento eliminado correctamente');
      },
      error: (err: any) => {
        console.error('Error eliminando movimiento:', err);
        this.mensajeError.set(err.error?.detalles?.message || 'Error al eliminar movimiento');
      }
    });
  }

  cancelarFormulario(limpiarMensajes: boolean = true): void {
    this.mostrarFormulario = false;

    this.movimientoForm.reset({
      cuentaId: null,
      tipoMovimiento: '',
      valor: null
    });

    if (limpiarMensajes) {
      this.limpiarMensajes();
    }
  }

  movimientosFiltrados = computed(() => {
    const texto = this.busqueda().trim().toLowerCase();

    return this.movimientos().filter(m =>
      String(m.id ?? '').includes(texto) ||
      String(m.fecha ?? '').toLowerCase().includes(texto) ||
      String(m.tipoMovimiento ?? '').toLowerCase().includes(texto) ||
      String(m.valor ?? '').includes(texto) ||
      String(m.saldo ?? '').includes(texto) ||
      String(m.numeroCuenta ?? '').includes(texto) ||
      String(m.nombreCliente ?? '').toLowerCase().includes(texto)
    );
  });

  campoInvalido(campo: string): boolean {
    const control = this.movimientoForm.get(campo);
    return !!control && control.invalid && (control.touched || control.dirty);
  }

  private limpiarMensajes(): void {
    this.mensajeError.set('');
    this.mensajeExito.set('');
  }
}