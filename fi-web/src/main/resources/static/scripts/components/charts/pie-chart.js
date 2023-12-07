import { Chart } from '/scripts/blocks/charts/chart.js';

export class PieChart extends Chart {
    constructor(id, props) {
        super(id, {
            initData: props.initData,
            load: props.load,
        });
        this.labels = props.labels ?? ['Date', 'Value'];
    }

    fill(points) {
        let container = document.querySelector(`#${this.id} .chart`);
        if (!container) return;

        // Create the data table
        let table = new google.visualization.DataTable();
        table.addColumn('string', this.labels[0]);
        table.addColumn('number', this.labels[1]);
        table.addRows(points);

        let chart = new google.visualization.PieChart(container)
        chart.draw(table, {
            pieHole: 0.5,
            colors: ["#34495E", "#1E6A49", "#7B8D8E", "#4B3A6E", "#6E4F6A", "#515A5A"],
            sliceVisibilityThreshold: 0.05,
            chartArea: { left: '10%', top: '10%', width: '80%', height: '80%' },
            tooltip: { trigger: 'selection' },
        });
        return chart;
    }
}