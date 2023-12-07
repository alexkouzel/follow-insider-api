import { Loader } from '/scripts/blocks/loader.js';

export class Chart {
    constructor(id, props) {
        this.id = id;
        this.filters = props.filters;
        this.initState = props.initState;
        this.initData = props.initData;
        this.loadData = props.load;
    }

    build() {
        let chart = document.createElement('div');

        // Filters are added before the chart (e.g. time range for line chart)
        if (this.filters) chart.appendChild(this.filters.build());

        chart.id = this.id;
        chart.innerHTML += '<div class="chart"></div>';
        this.loader = new Loader(chart);
        return chart;
    }

    load() {
        google.charts.load('current', { packages: ['corechart'] });

        // If props.load is defined, then data can be loaded from server
        if (this.loadData) {
            let loadData = (filters) => { this.reset(); this.loadData(filters) };

            // Initialize filters (if any)
            this.filters?.init(this.initState, loadData);

            // Fill initial data (if any). Otherwise, load data from server
            google.charts.setOnLoadCallback(() => this.initData
                ? this.update(this.initData)
                : loadData(this.initState));
        } else {
            google.charts.setOnLoadCallback(() => this.update(this.initData));
        }
    }

    update(data) {
        this.data = data;
        this.data.length !== 0
            ? this.redraw()
            : this.onEmpty();
    }

    onEmpty() {
        this.empty = true;
        this.loader.hide();
        let emptyState = document.createElement('div');
        emptyState.classList.add('abs-ctr', 'empty-state');
        emptyState.innerHTML = `
            <img src='/assets/icons/empty-box.svg' alt="table error">
            <h3>Whoops!</h3>
            <p>No data available at the moment</p>
        `;
        const chart = document.getElementById(this.id);
        chart.appendChild(emptyState);
    }

    reset() {
        if (this.empty) {
            this.empty = false;
            let emptyState = document.querySelector(`#${this.id} .empty-state`)
            emptyState?.remove();
        }
        this.chart?.clearChart();
        this.loader.show();
    }

    redraw() {
        if (this.empty) return;
        this.reset();
        this.draw();
    }

    draw() {
        if (!this.data || this.data.length === 0) return;
        this.chart = this.fill(this.data);
        this.loader.hide();
    }
}