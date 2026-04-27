import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Reporte, ReportesService } from '../../services/reporte.service';
import { ClientesService } from '../../services/clientes.service';
import { Cliente } from '../../models/cliente.model';


@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes.html',
  styleUrl: './reportes.css'
})
export class Reportes implements OnInit {

  clientes = signal<Cliente[]>([]);
  reporte = signal<Reporte[]>([]);

  clienteId: number | null = null;
  fechaInicio = '';
  fechaFin = '';

  mensajeError = signal('');

  constructor(
    private reportesService: ReportesService,
    private clientesService: ClientesService
  ) {}

  ngOnInit(): void {
    this.clientesService.getClientes().subscribe(data => {
      this.clientes.set(data);
    });
  }

  generarReporte() {
    if (!this.clienteId || !this.fechaInicio || !this.fechaFin) {
      this.mensajeError.set('Todos los campos son obligatorios');
      return;
    }

    this.reportesService
      .getReporte(this.clienteId, this.fechaInicio, this.fechaFin)
      .subscribe({
        next: (data) => this.reporte.set(data),
        error: () => this.mensajeError.set('Error generando reporte')
      });
  }

  descargarPDF() {
    this.reportesService
      .getReportePdf(this.clienteId!, this.fechaInicio, this.fechaFin)
      .subscribe(res => {
        const base64 = res.pdfBase64;

        const link = document.createElement('a');
        link.href = 'data:application/pdf;base64,' + base64;
        link.download = 'reporte.pdf';
        link.click();
      });
  }
}