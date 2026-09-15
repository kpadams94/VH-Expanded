Add-Type -AssemblyName System.Drawing
$root=$PSScriptRoot;$im=[Drawing.Bitmap]::new((Join-Path $root 'native/impact-draft.png'))
$edits=@();foreach($p in @(@(4,4),@(10,4),@(2,8),@(12,8),@(1,12),@(13,12),@(6,13),@(8,13))){$im.SetPixel($p[0],$p[1],[Drawing.ColorTranslator]::FromHtml('#FFF2BC'));$edits+=@{x=$p[0];y=$p[1];rgba='FFF2BCFF'}}
foreach($x in @(6,7,8)){$im.SetPixel($x,0,[Drawing.Color]::FromArgb(0,0,0,0));$edits+=@{x=$x;y=0;rgba='00000000'}}
$im.Save((Join-Path $root 'native/impact-v1.png'),[Drawing.Imaging.ImageFormat]::Png)
$rows=@();for($y=0;$y -lt 16;$y++){$row=@();for($x=0;$x -lt 16;$x++){$p=$im.GetPixel($x,$y);$row+=('{0:X2}{1:X2}{2:X2}{3:X2}' -f $p.R,$p.G,$p.B,$p.A)};$rows+=,@($row)}
@{width=16;height=16;pixels=$rows;format='RGBA hex rows';editorEdits=$edits;provenance='Native-grid drawing plus exact replay of eleven Piskel pencil/eraser actions; browser download timed out.'}|ConvertTo-Json -Depth 8|Set-Content -Encoding utf8 (Join-Path $root 'native/impact-v1.source.json')
$lunge=[Drawing.Bitmap]::new((Join-Path $root '../../docs/art/lunge/lunge.png'))
$b=[Drawing.Bitmap]::new(780,440);$g=[Drawing.Graphics]::FromImage($b);$g.Clear([Drawing.ColorTranslator]::FromHtml('#191C22'));$g.InterpolationMode='NearestNeighbor';$g.PixelOffsetMode='Half';$f=[Drawing.Font]::new('Segoe UI',12);$small=[Drawing.Font]::new('Segoe UI',10)
$g.DrawString('LUNGE / approved',$f,[Drawing.Brushes]::White,24,16);$g.DrawString('IMPACT / draft',$f,[Drawing.Brushes]::White,285,16)
$g.DrawString('Actual PNGs at 1x, 3x and 14x (nearest-neighbor)',$small,[Drawing.Brushes]::White,24,48)
$g.DrawImage($lunge,[Drawing.Rectangle]::new(25,90,16,16));$g.DrawImage($lunge,[Drawing.Rectangle]::new(75,78,48,48));$g.DrawImage($im,[Drawing.Rectangle]::new(286,90,16,16));$g.DrawImage($im,[Drawing.Rectangle]::new(336,78,48,48))
$g.DrawImage($lunge,[Drawing.Rectangle]::new(24,156,224,224));$g.DrawImage($im,[Drawing.Rectangle]::new(285,156,224,224))
$g.FillRectangle([Drawing.Brushes]::LightGray,550,78,192,192);$g.DrawImage($im,[Drawing.Rectangle]::new(550,78,192,192));$g.DrawString('Light background / 12x',$small,[Drawing.Brushes]::White,550,281)
$g.DrawString('Same palette, different silhouettes.',$small,[Drawing.Brushes]::White,24,403)
$g.Dispose();$b.Save((Join-Path $root 'impact-v1-review.png'));$b.Dispose();$im.Dispose();$lunge.Dispose()
