import { Chart } from '/scripts/blocks/charts/chart.js'
import { TimeRange } from '/scripts/blocks/charts/time-range.js'

export class BarChart extends Chart {
    constructor(id, props) {
        super(id, {
            filters: new TimeRange(),
            initState: props.initRange ?? '1Y',
            initData: props.initData,
            load: props.load,
        });
        this.labels = props.labels;
    }

    fill(data) {
        let container = document.querySelector(`#${this.id} .chart`);
        if (!container) return;

        var table = new google.visualization.DataTable();
        table.addColumn('string', 'Category');
        table.addColumn('number', this.labels[0]);
        table.addColumn('number', this.labels[1]);
        table.addRows(data);

        let chart = new google.visualization.ColumnChart(container)
        chart.draw(table, {
            legend: { position: 'top', maxLines: 3 },
            bar: { groupWidth: '75%' },
            isStacked: true,
            colors: ["#808000", "#b66141"],
            vAxis: {
                format: 'short',
                gridlines: { color: '#eee', minSpacing: 40 },
                minorGridlines: { count: 0 },
                viewWindow: { min: 0 },
                minValue: 0,
            },
            hAxis: {
                // format: empty ? '' : this.dateFormat(points),
                gridlines: { color: '#eee' },
                baselineColor: 'transparent'
            },
            chartArea: {
                top: 15,
                width: '85%',
                height: '80%',
            },
        });
        return chart;
    }
}