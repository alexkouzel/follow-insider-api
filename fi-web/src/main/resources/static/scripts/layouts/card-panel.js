import { LineChart } from '/scripts/blocks/charts/line-chart.js';
import { PieChart } from '/scripts/blocks/charts/pie-chart.js';
import { BarChart } from '/scripts/blocks/charts/bar-chart.js';
import { Config } from '/scripts/root.js'
import { Http } from '/scripts/utils/http.js';

export class CardPanel {
    constructor() {
        this.cards = [];
    }

    build() {
        let cardPanel = document.createElement('div');
        cardPanel.classList.add('card-panel');
        this.cards.forEach(card => cardPanel.appendChild(card.build()));
        return cardPanel;
    }

    alignAll() {
        this.cards.forEach(card => card.align());
    }

    addChart(props) {
        if (props.load) props.props.load = (state) =>
            this.defaultLoad(chart, props.load(state));

        let id = uniqueId();
        let chart = {
            pie: new PieChart(id, props.props),
            line: new LineChart(id, props.props),
            bar: new BarChart(id, props.props),
        }[props.type];

        this.charts.push(chart);
        this.cards.push(new Card(id + '-card', {
            content: chart.build(),
            name: props.name,
        }));
    }

    addTextCard(name, text) {
        if (!text || text === 'null') return;
        let content = document.createElement('p');
        content.innerHTML = text.split('\n').join('<br><br>');
        this.cards.push(new Card(uniqueId(), { content: content, name: name }));
    }

    defaultLoad(chart, props) {
        let update = (data) => chart.update(props.convert(data));
        Config.debugMode
            ? setTimeout(() => update(props.mock ?? []), Math.random() * 500 + 500)
            : Http.getJson(props.url).then((records) => update(records));
    }
}

export class Card {
    constructor(id, props) {
        this.id = id;
        this.name = props.name;
        this.content = props.content;
    }

    build() {
        let card = document.createElement('div');
        card.id = this.id;
        card.classList.add('card');
        card.innerHTML += `<h3 class="card-title">${this.name}</h3>`;
        card.appendChild(this.content);
        return card;
    }

    align() {
        let ref = document.getElementById(this.id);
        if (ref == null) return;
        let spans = Math.round(ref.clientHeight / 10) + 3;
        ref.style.gridRowEnd = 'span ' + spans;
    }
}
