import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Cuenta, CuentasService } from '../../services/cuentas.service';
import { ClientesService } from '../../services/clientes.service';
import { CapitalizarPipe } from '../../pipes/capitalizar-pipe';
import { Cliente } from '../../models/cliente.model';

@Component({
  selector: 'app-cuentas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, CapitalizarPipe],
  templateUrl: './cuentas.html',
  styleUrl: './cuentas.css'
})
export class Cuentas implements OnInit {

  cuentas = signal<Cuenta[]>([]);
  clientes = signal<Cliente[]>([]);
  busqueda = signal('');

  mostrarFormulario = false;
  modoEdicion = false;
  cuentaEditandoId: number | null = null;

  mensajeError = signal('');
  mensajeExito = signal('');

  cuentaForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private cuentasService: CuentasService,
    private clientesService: ClientesService
  ) {
    this.cuentaForm = this.fb.group({
      clienteId: [null, [Validators.required]],
      numeroCuenta: ['', [Validators.required, Validators.pattern(/^\d{6,10}$/)]],
      tipoCuenta: ['', [Validators.required]],
      saldoInicial: [null, [Validators.required, Validators.min(0)]],
      estado: [true, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.cargarCuentas();
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.clientesService.getClientes().subscribe({
      next: (data) => this.clientes.set(data),
      error: () => this.mensajeError.set('Error al cargar los clientes')
    });
  }

  cargarCuentas(): void {
    this.cuentasService.getCuentas().subscribe({
      next: (data) => this.cuentas.set(data),
      error: () => this.mensajeError.set('Error al cargar las cuentas')
    });
  }

  cuentasFiltradas = computed(() => {
    const texto = this.busqueda().trim().toLowerCase();

    return this.cuentas().filter(c =>
      String(c.id ?? '').includes(texto) ||
      String(c.numeroCuenta ?? '').toLowerCase().includes(texto) ||
      String(c.tipoCuenta ?? '').toLowerCase().includes(texto) ||
      String(c.saldoInicial ?? '').includes(texto) ||
      String(c.saldoDisponible ?? '').includes(texto) ||
      String(c.nombreCliente ?? c.clienteId ?? '').toLowerCase().includes(texto)
    );
  });

  abrirNuevo(): void {
    this.limpiarMensajes();
    this.mostrarFormulario = true;
    this.modoEdicion = false;
    this.cuentaEditandoId = null;

    this.cuentaForm.reset({
      clienteId: null,
      numeroCuenta: '',
      tipoCuenta: '',
      saldoInicial: null,
      estado: true
    });

    this.cuentaForm.get('clienteId')?.enable();
    this.cuentaForm.get('saldoInicial')?.enable();
  }

  guardarCuenta(): void {
    this.limpiarMensajes();

    if (this.cuentaForm.invalid) {
      this.cuentaForm.markAllAsTouched();
      this.mensajeError.set('Revisa los campos del formulario');
      return;
    }
    const formValue = this.cuentaForm.getRawValue();

    const cuentaEnviar = {
      numeroCuenta: formValue.numeroCuenta.trim(),
      tipoCuenta: formValue.tipoCuenta.trim().toUpperCase(),
      saldoInicial: Number(formValue.saldoInicial),
      estado: Boolean(formValue.estado)
    };

    const clienteId = Number(this.cuentaForm.value.clienteId);

    if (this.modoEdicion && this.cuentaEditandoId) {
      this.actualizarCuenta(this.cuentaEditandoId, cuentaEnviar);
    } else {
      this.crearCuenta(clienteId, cuentaEnviar);
    }
  }

  crearCuenta(clienteId: number, cuentaEnviar: any): void {
    this.cuentasService.crearCuenta(clienteId, cuentaEnviar).subscribe({
      next: () => {
        this.mensajeExito.set('Cuenta creada correctamente');
        this.cargarCuentas();
        this.cancelarFormulario();
      },
      error: (err) => {
        this.mensajeError.set(err.error?.mensaje || 'Error al crear cuenta');
      }
    });
  }

  editarCuenta(cuenta: Cuenta): void {
    this.limpiarMensajes();

    this.mostrarFormulario = true;
    this.modoEdicion = true;
    this.cuentaEditandoId = cuenta.id ?? null;

    this.cuentaForm.patchValue({
      clienteId: cuenta.clienteId ?? null,
      numeroCuenta: cuenta.numeroCuenta,
      tipoCuenta: cuenta.tipoCuenta,
      saldoInicial: cuenta.saldoInicial,
      estado: cuenta.estado
    });

    this.cuentaForm.get('clienteId')?.disable();
    this.cuentaForm.get('saldoInicial')?.disable();
  }

actualizarCuenta(id: number, cuentaEnviar: any): void {
  this.cuentasService.actualizarCuenta(id, cuentaEnviar).subscribe({
    next: () => {
      this.cargarCuentas();
      this.cancelarFormulario();
      this.mensajeExito.set('Cuenta actualizada correctamente');
    },
    error: (err: any) => {
      this.mensajeError.set(err.error?.mensaje?.message || 'Error al actualizar cuenta');
    }
  });
}

  eliminarCuenta(id: number): void {
    this.limpiarMensajes();

    if (!confirm('¿Está seguro de eliminar esta cuenta?')) {
      return;
    }

    this.cuentasService.eliminarCuenta(id).subscribe({
      next: () => {
        this.mensajeExito.set('Cuenta eliminada correctamente');
        this.cargarCuentas();
      },
      error: (err) => {
        this.mensajeError.set(err.error?.mensaje || 'Error al eliminar cuenta');
      }
    });
  }

  cancelarFormulario(): void {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.cuentaEditandoId = null;

    this.cuentaForm.reset({
      clienteId: null,
      numeroCuenta: '',
      tipoCuenta: '',
      saldoInicial: null,
      estado: true
    });

    this.cuentaForm.get('clienteId')?.enable();
    this.cuentaForm.get('saldoInicial')?.enable();
    //this.limpiarMensajes();
  }

  campoInvalido(campo: string): boolean {
    const control = this.cuentaForm.get(campo);
    return !!control && control.invalid && (control.touched || control.dirty);
  }

  private limpiarMensajes(): void {
    this.mensajeError.set('');
    this.mensajeExito.set('');
  }
}