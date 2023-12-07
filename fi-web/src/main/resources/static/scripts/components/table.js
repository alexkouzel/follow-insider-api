import { Loader } from '/scripts/blocks/loader.js';

export class Table {
    constructor(id, columns, props) {
        this.id = id;
        this.columns = columns;
        this.props = props;
        this.filters = props.filters;
        this.align = props.align ?? Array(columns.length).fill('start');
        this.full = false;
        this.rowCount = 0;
    }

    build() {
        const table = document.createElement('div');

        if (this.filters) {
            table.appendChild(this.filters.build());
        }

        let headerRow = this.columns.map((label, i) => 
            `<th style="text-align: ${this.align[i]}">${label}</th>`).join('');

        table.innerHTML += `
            <div id="${this.id}" class="table-wrapper scrollbar">
                <table>
                    <thead><tr>
                        <th style="width: ${this.props.padding_left ?? 'auto'}"></th>
                        ${headerRow}
                        <th style="width: ${this.props.padding_right ?? 'auto'}"></th>
                    </tr></thead>
                    <tbody></tbody>
                </table>
            </div>`;

        const wrapper = table.querySelector('#' + this.id);
        this.loader = new Loader(wrapper);
        return table;
    }

    onFull() {
        this.loader.hide();
        if (this.rowCount === 0) {
            document.getElementById(this.id).innerHTML += `
                <div class='abs-ctr empty-state'>
                    <img src='/assets/icons/empty-box.svg' alt="table error">
                    <h3>Whoops!</h3>
                    <p>No data available at the moment</p>
                <div>`
        }
        this.full = true;
    }

    onError() {
        this.loader.hide();
        document.getElementById(this.id).innerHTML += `
            <div class='abs-ctr empty-state'>
                <img src='/assets/icons/empty-cloud.svg' alt="table error">
                <h3>Nooooo!</h3>
                <p>Something went wrong on the server</p>
            <div>`;
    }

    addRows(rows) {
        this.loader.hide();
        const body = document.querySelector(`#${this.id} tbody`);
        rows.forEach(row => {
            const rowRef = row.build(this.props.align);
            body.appendChild(rowRef);
            this.rowCount++;
        });
    }

    reset() {
        this.full = false;
        this.rowCount = 0;
        document.querySelector(`#${this.id} .empty-state`)?.remove();
        document.querySelector(`#${this.id} tbody`).innerHTML = '';
        this.loader.show();
    }
}

export class Row {
    constructor(id, cells, props) {
        this.id = id;
        this.props = props ?? {};
        this.cells = cells.filter(cell => cell);
    }

    build(align) {
        let row = document.createElement('tr');
        row.style.backgroundColor = this.props.color;
        row.id = this.id;
        row.appendChild(new Cell('', { max_width: 0 }).build())
        for (let i = 0; i < this.cells.length; i++) {
            row.appendChild(this.cells[i].build(align[i]));
        }
        row.appendChild(new Cell('', { max_width: 0 }).build())
        return row;
    }
}

export class Cell {
    constructor(text, props) {
        this.text = text;
        this.props = props ?? {};
    }

    build(align) {
        let cell = document.createElement('td');
        if (this.props.onclick) cell.onclick = this.props.onclick;
        if (this.props.color) cell.style.color = this.props.color;
        if (this.props.max_width) cell.style.maxWidth = this.props.max_width;
        if (this.props.min_width) cell.style.minWidth = this.props.min_width;
        if (this.props.class) cell.classList.add(this.props.class);
        if (this.props.id) cell.id = this.props.id;
        cell.style.textAlign = align;
        cell.innerText = this.text;
        return cell;
    }
}

export class Checkbox {
    constructor(label, props) {
        this.label = label;
        this.checked = props.checked ?? false;
        this.color = props.color;
    }

    build() {
        let box = document.createElement('div');
        let input = document.createElement('input');
        let label = document.createElement('label');
        input.type = 'checkbox';
        input.id = this.label;
        input.name = this.label.toUpperCase();
        input.click();
        label.style.color = this.color;
        label.innerText = this.label;
        label.setAttribute('for', this.label);
        box.appendChild(input);
        box.appendChild(label);
        return box;
    }
}

export class CheckboxFilters {
    constructor(id, boxes) {
        this.id = id;
        this.boxes = boxes;
    }

    build() {
        let filters = document.createElement('div');
        this.boxes.forEach(box => filters.appendChild(box.build()));
        filters.classList.add('filters');
        filters.id = this.id;
        return filters;
    }

    onselect(func) {
        document.querySelectorAll(`#${this.id} input`).forEach(box => {
            box.onchange = func;
        });
    }

    get checked() {
        let checked = document.querySelectorAll(`#${this.id} input:checked`);
        return [...checked].map(box => box.getAttribute('name'));
    }
}