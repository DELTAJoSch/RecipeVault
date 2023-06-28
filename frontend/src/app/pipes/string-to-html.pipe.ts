import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'stringToHtml'
})
export class StringToHtmlPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    const tokens = value.split('\n');

    let val = '<p>' + tokens.join('</p> <p>') + '</p>';
    val = val.split('\r').join('');

    return val;
  }

}
