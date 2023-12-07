import { TimeRange } from '/scripts/blocks/charts/time-range.js';
import { Chart } from '/scripts/blocks/charts/chart.js';

export class LineChart extends Chart {
    constructor(id, props) {
        super(id, {
            filters: new TimeRange(),
            initState: props.initRange ?? '1Y',
            initData: props.initData,
            load: props.load,
        });
        this.addTrendline = props.addTrendline ?? false;
        this.onlyDots = props.onlyDots ?? false;
        this.dotSize = props.dotSize ?? 0;
        this.labels = props.labels ?? ['Date', 'Value'];
    }

    fill(points) {
        let container = document.querySelector(`#${this.id} .chart`);
        if (!container) return;

        // Create the data table
        let table = new google.visualization.DataTable();
        table.addColumn('date', this.labels[0]);
        table.addColumn('number', this.labels[1]);

        // Add rows to the data table
        points = points.sort((a, b) => a[0] > b[0] ? 1 : -1);
        let rows = points.map(point => [...point]);
        if (this.addTrendline) {
            table.addColumn('number', 'Trend');
            const trendline = this.calculateTrendline(points, 2);
            rows.forEach((row, i) => row.push(trendline[i][1]));
        }
        table.addRows(rows);

        // chart initialization 
        let empty = points.length === 0;
        let chart = new google.visualization.LineChart(container);
        chart.draw(table, {
            legend: 'none',
            tooltip: { trigger: empty ? 'none' : 'hover' },
            focusTarget: 'category',
            pointSize: empty ? 0 : this.dotSize,
            lineWidth: this.onlyDots ? 0 : 1,
            series: {
                0: { // Original data
                    color: this.addTrendline ? '#CBC3E3' : '#5D3FD3',
                },
                1: { // Trendline
                    color: '#5D3FD3',
                    enableInteractivity: false,
                    visibleInLegend: false,
                    lineWidth: 2,
                    pointSize: 0,
                    tooltip: 'none',
                },
            },
            vAxis: {
                format: 'short',
                gridlines: { color: '#eee', minSpacing: 40 },
                minorGridlines: { count: 0 },
                viewWindow: { min: 0 },
                minValue: 0,
            },
            hAxis: {
                format: empty ? '' : this.dateFormat(points),
                gridlines: { color: '#eee' },
                baselineColor: 'transparent'
            },
            crosshair: {
                color: '#CBC3E3',
                trigger: 'focus',
                orientation: 'vertical'
            },
            chartArea: {
                top: 15,
                width: '85%',
                height: '80%',
            },
        });
        return chart;
    }

    dateFormat(points) {
        let months = 0;
        if (points.length > 1) {
            const to = points[points.length - 1][0].getTime();
            const from = points[0][0].getTime();
            months = (to - from) / 1000 / 3600 / 24 / 30;
        }
        if (months > 70) return 'y';
        if (months > 8) return 'MMM y';
        if (months > 3) return 'MMM d, y';
        return 'MMM d';
    }

    calculateTrendline(points, degree) {
        const x = points.map(([date, _]) => date.getTime());
        const y = points.map(([_, value]) => value);

        const n = x.length;
        const X = new Array(degree + 1).fill(0).map(() => new Array(degree + 1).fill(0));
        const Y = new Array(degree + 1).fill(0);

        for (let k = 0; k <= 2 * degree; k++) {
            let sumX = 0;
            for (let i = 0; i < n; i++) {
                sumX += Math.pow(x[i], k);
            }

            for (let i = Math.max(0, k - degree); i <= Math.min(degree, k); i++) {
                const j = k - i;
                X[i][j] += sumX;
            }

            if (k <= degree) {
                let sumY = 0;
                for (let i = 0; i < n; i++) {
                    sumY += y[i] * Math.pow(x[i], k);
                }
                Y[k] = sumY;
            }
        }

        const coefficients = this.gaussianElimination(X, Y);

        const ySmooth = x.map((xVal) =>
            coefficients.reduce((sum, coeff, index) => sum + coeff * Math.pow(xVal, index), 0)
        );

        return x.map((xVal, i) => [new Date(xVal), ySmooth[i]]);
    }

    gaussianElimination(A, B) {
        const n = A.length;
        for (let i = 0; i < n; i++) {
            let maxRow = i;
            for (let j = i + 1; j < n; j++) {
                if (Math.abs(A[j][i]) > Math.abs(A[maxRow][i])) {
                    maxRow = j;
                }
            }
            [A[maxRow], A[i]] = [A[i], A[maxRow]];
            [B[maxRow], B[i]] = [B[i], B[maxRow]];

            for (let j = i + 1; j < n; j++) {
                const factor = A[j][i] / A[i][i];
                for (let k = i; k < n; k++) {
                    A[j][k] -= factor * A[i][k];
                }
                B[j] -= factor * B[i];
            }
        }

        const coefficients = new Array(n);
        for (let i = n - 1; i >= 0; i--) {
            let sum = 0;
            for (let j = i + 1; j < n; j++) {
                sum += A[i][j] * coefficients[j];
            }
            coefficients[i] = (B[i] - sum) / A[i][i];
        }

        return coefficients;
    }
}